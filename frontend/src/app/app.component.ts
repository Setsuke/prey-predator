import { CommonModule } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subscription, timer } from 'rxjs';
import { SimulationRequest, SimulationResponse, PopulationPoint } from './simulation.models';
import { SimulationService } from './simulation.service';

type RenderCell = {
  row: number;
  column: number;
  grassLevel: number;
  hasRabbit: boolean;
  hasFox: boolean;
};

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnDestroy {
  readonly parameters: SimulationRequest = {
    rows: 60,
    columns: 60,
    initialRabbits: 200,
    initialFoxes: 15,
    rabbitSpeed: 1,
    foxSpeed: 3,
    grassGrowthRate: 60,
    maxGrass: 5,
    startGrass: 1,
    rabbitInitialFood: 10,
    foxInitialFood: 10,
    rabbitVision: 5,
    foxVision: 7,
    rabbitBreedFrequency: 10,
    foxBreedFrequency: 30,
    rabbitBreedFood: 10,
    foxBreedFood: 20,
    rabbitMaxAge: 40,
    foxMaxAge: 80,
    seed: 42
  };

  simulation: SimulationResponse | null = null;
  history: PopulationPoint[] = [];
  isRunning = false;
  isLoading = false;
  errorMessage = '';

  private runSubscription: Subscription | null = null;

  constructor(private readonly simulationService: SimulationService) {}

  ngOnDestroy(): void {
    this.stop();
  }

  createSimulation(): void {
    this.stop();
    this.isLoading = true;
    this.errorMessage = '';

    this.simulationService.createSimulation(this.parameters).subscribe({
      next: (simulation) => {
        this.setSimulation(simulation, true);
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Unable to create the simulation. Check that the backend is running.';
        this.isLoading = false;
      }
    });
  }

  stepSimulation(): void {
    if (!this.simulation || this.isLoading) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.simulationService.stepSimulation(this.simulation.id).subscribe({
      next: (simulation) => {
        this.setSimulation(simulation, false);
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Unable to advance the simulation.';
        this.isLoading = false;
        this.stop();
      }
    });
  }

  toggleRun(): void {
    if (this.isRunning) {
      this.stop();
      return;
    }

    if (!this.simulation) {
      this.createSimulation();
    }

    this.isRunning = true;
    this.runSubscription = timer(350, 350).subscribe(() => this.stepSimulation());
  }

  stop(): void {
    this.isRunning = false;
    this.runSubscription?.unsubscribe();
    this.runSubscription = null;
  }

  renderCells(): RenderCell[] {
    if (!this.simulation) {
      return [];
    }

    const grass = new Map<string, number>();
    const animals = new Map<string, { hasRabbit: boolean; hasFox: boolean }>();

    for (const cell of this.simulation.cells) {
      grass.set(this.key(cell.row, cell.column), cell.grassLevel);
    }

    for (const animal of this.simulation.animals) {
      const key = this.key(animal.row, animal.column);
      const current = animals.get(key) ?? { hasRabbit: false, hasFox: false };
      current.hasRabbit = current.hasRabbit || animal.species === 'RABBIT';
      current.hasFox = current.hasFox || animal.species === 'FOX';
      animals.set(key, current);
    }

    const cells: RenderCell[] = [];

    for (let row = 0; row < this.simulation.rows; row++) {
      for (let column = 0; column < this.simulation.columns; column++) {
        const key = this.key(row, column);
        const animal = animals.get(key);

        cells.push({
          row,
          column,
          grassLevel: grass.get(key) ?? 0,
          hasRabbit: animal?.hasRabbit ?? false,
          hasFox: animal?.hasFox ?? false
        });
      }
    }

    return cells;
  }

  cellClass(cell: RenderCell): string {
    const classes = ['grid-cell', `grass-${Math.min(cell.grassLevel, 5)}`];

    if (cell.hasRabbit) {
      classes.push('has-rabbit');
    }

    if (cell.hasFox) {
      classes.push('has-fox');
    }

    return classes.join(' ');
  }

  chartPoints(type: 'rabbits' | 'foxes' | 'grass'): string {
    if (this.history.length < 2) {
      return '';
    }

    const values = this.history.map((point) => point[type]);
    const minValue = Math.min(...values);
    const maxValue = Math.max(...values);
    const range = Math.max(maxValue - minValue, 1);
    const lastIndex = this.history.length - 1;

    return this.history
      .map((point, index) => {
        const x = (index / lastIndex) * 100;
        const normalizedValue = (point[type] - minValue) / range;
        const y = 95 - normalizedValue * 90;
        return `${x},${y}`;
      })
      .join(' ');
  }

  private setSimulation(simulation: SimulationResponse, resetHistory: boolean): void {
    this.simulation = simulation;
    const point = {
      step: simulation.step,
      rabbits: simulation.rabbitCount,
      foxes: simulation.foxCount,
      grass: simulation.grassTotal
    };

    this.history = resetHistory ? [point] : [...this.history.slice(-999), point];
  }

  private key(row: number, column: number): string {
    return `${row}:${column}`;
  }
}
