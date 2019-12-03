import {HelpPageId} from 'projects/help/src/lib/help-panel/help-page-id';

export const HELP_PAGES: { [key in HelpPageId]: string } = {
  HOME: '/',
  TEST: '/test',
  EDITOR_MARKDOWN: '/editors/markdown/',
  EDITOR_GATLING: '/editors/gatling-scala/',
  EDITOR_DEBUG: '/editors/debug-request-response/',
  ADMIN_FILE_EDITOR: '/administration/files-editor-usage/',

  // Tabs
  'administration-storage': '/administration/files-manager-usage/',
  'resources-tree': '/gatling/#resources-tree',
  'simulations-tree': '/gatling/#simulations-tree',

  ADMIN_CONFIGURATION: '/administration/files-manager-usage/',
  GATLING_RESOURCES: '/gatling/#resources-tree',
  GATLING_SIMULATIONS: '/gatling/#simulations-tree',
  GATLING_EXECUTIONS: '/gatling/#gatling-executions',
  GATLING_RESULT_LIST: '/gatling/#gatling-test-results',
  GATLING_DEBUG_LIST: '/gatling/#debug-table',
  ADMIN_CONTAINERS: '/administration/#docker-containers',
  ADMIN_IMAGES: '/administration/#docker-images',
  ADMIN_EXECUTIONS: '/administration/execute-commands/',

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
  ADMIN_RUN_CONTAINER: '/administration/docker-containers-table/#run-docker-container',
  ADMIN_INSPECT_CONTAINER: '/administration/docker-containers-table/#inspect-docker-container',
  ADMIN_INSPECT_IMAGE: '/administration/docker-images-table/#inspect-docker-image',
  ADMIN_PULL_IMAGE: '/administration/docker-images-table/#pull-docker-image',
  ADMIN_RUN_SCRIPT: '/administration/execute-commands/#run-a-shell-script',
  ADMIN_RUN_COMMAND: '/administration/execute-commands/#run-a-shell-command',
};
