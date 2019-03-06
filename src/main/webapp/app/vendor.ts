/* after changing this file run 'npm run webpack:build' */
/* tslint:disable */
import '../content/scss/vendor.scss';

// Imports all fontawesome core and solid icons

import { library } from '@fortawesome/fontawesome-svg-core';
import {
    faUser,
    faSort,
    faSortUp,
    faSortDown,
    faSync,
    faEye,
    faBan,
    faTimes,
    faArrowLeft,
    faSave,
    faPlus,
    faPencilAlt,
    faBars,
    faThList,
    faUserPlus,
    faRoad,
    faTachometerAlt,
    faHeart,
    faList,
    faBell,
    faBook,
    faHdd,
    faFlag,
    faWrench,
    faClock,
    faCloud,
    faSignOutAlt,
    faSignInAlt,
    faCalendarAlt,
    faSearch,
    faTrashAlt,
    faAsterisk,
    faTasks,
    faHome,
    faChartBar,
    faTv,
    faCamera,
    faBirthdayCake,
    faVenusMars
} from '@fortawesome/free-solid-svg-icons';

// Adds the SVG icon to the library so you can use it in your page
library.add(faArrowLeft);
library.add(faAsterisk);
library.add(faBan);
library.add(faBars);
library.add(faBell);
library.add(faBook);
library.add(faCalendarAlt);
library.add(faChartBar);
library.add(faClock);
library.add(faCloud);
library.add(faEye);
library.add(faFlag);
library.add(faHdd);
library.add(faHeart);
library.add(faHome);
library.add(faList);
library.add(faPencilAlt);
library.add(faPlus);
library.add(faRoad);
library.add(faSave);
library.add(faSearch);
library.add(faSignInAlt);
library.add(faSignOutAlt);
library.add(faSort);
library.add(faSortDown);
library.add(faSortUp);
library.add(faSync);
library.add(faTachometerAlt);
library.add(faTasks);
library.add(faThList);
library.add(faTimes);
library.add(faTrashAlt);
library.add(faUser);
library.add(faUserPlus);
library.add(faWrench);
library.add(faTv);
library.add(faCamera);
library.add(faBirthdayCake);
library.add(faVenusMars);

// add ng2-charts chart.js
import 'chart.js/src/chart.js';

// jhipster-needle-add-element-to-vendor - JHipster will add new menu items here
