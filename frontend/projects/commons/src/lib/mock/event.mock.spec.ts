export const eventSpy = () => jasmine.createSpyObj('event', ['preventDefault', 'stopPropagation']);

export const eventEmitterSpy = () => jasmine.createSpyObj('EventEmitter', ['emit', 'subscribe']);
