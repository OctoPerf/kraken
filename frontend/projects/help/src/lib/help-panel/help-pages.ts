import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';

export const HELP_PAGES: { [key in HelpPageId]: string } = {
  HOME: '/',
  TEST: '/test',
  EDITOR_MARKDOWN: '/editors/markdown/',
  EDITOR_GATLING: '/editors/gatling-scala/',
  EDITOR_DEBUG: '/editors/debug-request-response/',
  ADMIN_FILE_EDITOR: '/administration/files-editor-usage/',

  'administration-storage': '/administration/files-manager-usage/',
  ADMIN_CONFIGURATION: '/administration/files-manager-usage/',
  ADMIN_HOSTS_TABLE: '/administration/hosts-table/',
  'resources-tree': '/gatling/#resources-tree',
  GATLING_RESOURCES: '/gatling/#resources-tree',
  'simulations-tree': '/gatling/#simulations-tree',
  GATLING_SIMULATIONS: '/gatling/#simulations-tree',
  'gatling-files-tree': '/gatling/advanced-configuration-files/',
  GATLING_CONFIGURATION: '/gatling/advanced-configuration-files/',
  GATLING_EXECUTIONS: '/gatling/#gatling-executions',
  GATLING_RESULTS_TABLE: '/gatling/#gatling-test-results',
  GATLING_DEBUG_ENTRIES_TABLE: '/gatling/#debug-entries-table',
  GATLING_TASKS_TABLE: '/gatling/#tasks-table',
  GATLING_CONTAINERS_TABLE: '/gatling/#tasks-table',
  GATLING_LOGS: '/gatling/#logs',

  // Dialogs
  GATLING_IMPORT_HAR: '/gatling/import-har-gatling-scripts/#import-http-archives',
  GATLING_DEBUG_DIALOG: '/gatling/debug-gatling-scripts/#execution-dialog',
  GATLING_COMPARE_DEBUG: '/gatling/debug-gatling-scripts/#compare-debug-results',
  GATLING_RUN_DIALOG: '/gatling/execute-gatling-scripts/#execution-dialog',
  ADMIN_CREATE_FILE: '/administration/files-manager-usage/#create-files-and-directories',
  ADMIN_DELETE_FILE: '/administration/files-manager-usage/#delete-files',
  ADMIN_DELETE_CONTAINER: '/administration/docker-containers-table/#start-stop-delete-docker-container',
  ADMIN_DELETE_IMAGE: '/administration/docker-images-table/#delete-docker-image',
  ADMIN_UPLOAD_FILE: '/administration/files-manager-usage/#files-upload',
  ADMIN_DOWNLOAD_FILE: '/administration/files-manager-usage/#files-and-folders-download',
  ADMIN_ATTACH_HOST: '/administration/hosts-table/',

  PROJECT_LIST: '/project/#list-projects',
  PROJECT_CREATE: '/project/#create-a-new-project',
  PROJECT_IMPORT: '/project/#import-a-project-from-git',
  PROJECT_CONNECT: '/git/#connect-an-existing-project-to-git',

  GIT_COMMAND: '/git/#the-git-command-panel',
  GIT_STATUS: '/git/#the-git-status-panel'
};
