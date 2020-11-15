import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {RunSimulationNodeButtonComponent} from './run-simulation-node-button.component';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';

describe('RunSimulationNodeButtonComponent', () => {
  let component: RunSimulationNodeButtonComponent;
  let fixture: ComponentFixture<RunSimulationNodeButtonComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [RunSimulationNodeButtonComponent],
      providers: [
        {provide: SimulationService, useValue: simulationServiceSpy()},
      ]
    })
      .overrideTemplate(RunSimulationNodeButtonComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunSimulationNodeButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
