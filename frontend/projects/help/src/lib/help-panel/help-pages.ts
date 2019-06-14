export type HelpPageId = 'HOME'
  | 'TEST'
  | 'EDITOR_MARKDOWN'
  | 'EDITOR_DOCKER_COMPOSE'
  | 'EDITOR_GATLING'
  | 'EDITOR_DEBUG'
  | 'FILE_EDITOR';


export const HELP_PAGES: { [key in HelpPageId]: string } = {
  HOME: '/',
  TEST: '/test',
  EDITOR_MARKDOWN: '/editors/markdown/',
  EDITOR_DOCKER_COMPOSE: '/editors/docker-compose/',
  EDITOR_GATLING: '/editors/gatling-scala/',
  EDITOR_DEBUG: '/editors/debug-request-response/',
  FILE_EDITOR: '/administration/files-editor-usage/',
};
