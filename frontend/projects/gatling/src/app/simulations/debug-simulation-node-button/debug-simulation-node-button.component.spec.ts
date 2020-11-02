import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {DebugSimulationNodeButtonComponent} from 'projects/gatling/src/app/simulations/debug-simulation-node-button/debug-simulation-node-button.component';
import {SimulationService} from 'projects/gatling/src/app/simulations/simulation.service';
import {simulationServiceSpy} from 'projects/gatling/src/app/simulations/simulation.service.spec';

describe('DebugSimulationNodeButtonComponent', () => {
  let component: DebugSimulationNodeButtonComponent;
  let fixture: ComponentFixture<DebugSimulationNodeButtonComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [DebugSimulationNodeButtonComponent],
      providers: [
        {provide: SimulationService, useValue: simulationServiceSpy()},
      ]
    })
      .overrideTemplate(DebugSimulationNodeButtonComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DebugSimulationNodeButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
