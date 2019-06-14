import {HelpPageId} from './help-pages';

export interface HelpEvent {
  pageId?: HelpPageId;
  channel: string;
}
