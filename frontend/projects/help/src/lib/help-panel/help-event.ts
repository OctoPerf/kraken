import {HelpPageId} from './help-page-id';

export interface HelpEvent {
  pageId?: HelpPageId;
  channel: string;
}
