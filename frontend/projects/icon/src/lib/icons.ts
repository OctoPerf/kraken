import {IconFa} from 'projects/icon/src/lib/icon-fa';
import {faSync} from '@fortawesome/free-solid-svg-icons/faSync';
import {library} from '@fortawesome/fontawesome-svg-core';
import {IconDynamic} from 'projects/icon/src/lib/icon-dynamic';
import {faTrash} from '@fortawesome/free-solid-svg-icons/faTrash';
import {faSearch} from '@fortawesome/free-solid-svg-icons/faSearch';
import {faTimesCircle} from '@fortawesome/free-regular-svg-icons/faTimesCircle';
import {faEllipsisV} from '@fortawesome/free-solid-svg-icons/faEllipsisV';
import {faPlay} from '@fortawesome/free-solid-svg-icons/faPlay';
import {IconFaAddon} from 'projects/icon/src/lib/icon-fa-addon';
import {faSquare} from '@fortawesome/free-regular-svg-icons/faSquare';
import {faICursor} from '@fortawesome/free-solid-svg-icons/faICursor';
import {faBug} from '@fortawesome/free-solid-svg-icons/faBug';

library.add(faSync, faTimesCircle, faTrash, faSearch, faEllipsisV, faPlay, faSquare, faICursor, faBug);

export const LOADING_ICON = new IconFa(faSync, 'muted', '', true);

export const REFRESH_ICON = new IconDynamic(new IconFa(faSync, 'primary'), {
  loading: LOADING_ICON
});

export const MENU_ICON = new IconFa(faEllipsisV, 'primary');

export const DELETE_ICON = new IconFa(faTrash, 'error');

export const INSPECT_ICON = new IconFa(faSearch);

export const CLOSE_ICON = new IconDynamic(new IconFa(faTimesCircle), {'selected': new IconFa(faTimesCircle, 'error')});

export const PLAY_ICON = new IconFa(faPlay, 'success');

export const DEBUG_ICON = new IconFa(faBug, 'primary');

export const RENAME_ICON = new IconFaAddon(
  new IconFa(faSquare, 'foreground', 'shrink-3 down-2'),
  new IconFa(faICursor, 'accent', 'right-5 grow-2 down-2'),
);

