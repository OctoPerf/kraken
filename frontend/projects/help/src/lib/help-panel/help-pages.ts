export type HelpPageId = 'HOME'
  | 'TEST';


export const HELP_PAGES: { [key in HelpPageId]: string } = {
  HOME: '/',
  TEST: '/test',
};
