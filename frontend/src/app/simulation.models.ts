export type Species = 'RABBIT' | 'FOX';

export interface SimulationRequest {
  rows: number;
  columns: number;
  initialRabbits: number;
  initialFoxes: number;
  rabbitSpeed: number;
  foxSpeed: number;
  grassGrowthRate: number;
  maxGrass: number;
  startGrass: number;
  rabbitInitialFood: number;
  foxInitialFood: number;
  rabbitVision: number;
  foxVision: number;
  rabbitBreedFrequency: number;
  foxBreedFrequency: number;
  rabbitBreedFood: number;
  foxBreedFood: number;
  rabbitMaxAge: number;
  foxMaxAge: number;
  seed: number;
}

export interface CellResponse {
  row: number;
  column: number;
  grassLevel: number;
}

export interface AnimalResponse {
  id: string;
  species: Species;
  row: number;
  column: number;
  food: number;
}

export interface SimulationResponse {
  id: string;
  step: number;
  rows: number;
  columns: number;
  rabbitCount: number;
  foxCount: number;
  grassTotal: number;
  cells: CellResponse[];
  animals: AnimalResponse[];
}

export interface PopulationPoint {
  step: number;
  rabbits: number;
  foxes: number;
  grass: number;
}
