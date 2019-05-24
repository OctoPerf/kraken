import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {CommandTabHeaderComponent} from 'projects/command/src/lib/command-tab-header/command-tab-header.component';
import {Command} from 'projects/command/src/lib/entities/command';
import {CommandService} from 'projects/command/src/lib/command.service';
import {commandServiceSpy} from 'projects/command/src/lib/command.service.spec';
import SpyObj = jasmine.SpyObj;


describe('CommandTabHeaderComponent', () => {
  let component: CommandTabHeaderComponent;
  let fixture: ComponentFixture<CommandTabHeaderComponent>;
  let commands: SpyObj<CommandService>;

  beforeEach(async(() => {
    commands = commandServiceSpy();

    TestBed.configureTestingModule({
      declarations: [CommandTabHeaderComponent],
      providers: [
        {provide: CommandService, useValue: commands}
      ]
    })
      .overrideTemplate(CommandTabHeaderComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommandTabHeaderComponent);
    component = fixture.componentInstance;
  });

  it('should use logs command', () => {
    commands.getCommandName.and.returnValue('commandName');
    commands.getCommandTitle.and.returnValue('commandTitle');
    component.log = {command: new Command(['java', '--version']), status: 'INITIALIZED', text: ''};
    fixture.detectChanges();
    expect(component.name).toBe('commandName');
    expect(component.title).toBe('commandTitle');
  });

  it('should remove command name', () => {
    component.log = {command: new Command(['java', '--version']), status: 'INITIALIZED', text: ''};
    fixture.detectChanges();
    const unsubscribe = spyOn(component._subscription, 'unsubscribe');
    component.ngOnDestroy();
    expect(commands.removeCommandLabel).toHaveBeenCalled();
    expect(unsubscribe).toHaveBeenCalled();
  });

});
