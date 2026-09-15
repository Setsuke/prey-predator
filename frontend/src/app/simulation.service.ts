import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SimulationRequest, SimulationResponse } from './simulation.models';

@Injectable({ providedIn: 'root' })
export class SimulationService {
  private readonly apiUrl = 'http://localhost:8080/api/simulations';

  constructor(private readonly http: HttpClient) {}

  createSimulation(request: SimulationRequest): Observable<SimulationResponse> {
    return this.http.post<SimulationResponse>(this.apiUrl, request);
  }

  stepSimulation(id: string): Observable<SimulationResponse> {
    return this.http.post<SimulationResponse>(`${this.apiUrl}/${id}/steps`, {});
  }
}
