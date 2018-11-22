import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThelearningzoneSharedModule } from 'app/shared';
import { ThelearningzoneAdminModule } from 'app/admin/admin.module';
import {
    UserInfoComponent,
    UserInfoDetailComponent,
    UserInfoUpdateComponent,
    UserInfoDeletePopupComponent,
    UserInfoDeleteDialogComponent,
    userInfoRoute,
    userInfoPopupRoute
} from './';

const ENTITY_STATES = [...userInfoRoute, ...userInfoPopupRoute];

@NgModule({
    imports: [ThelearningzoneSharedModule, ThelearningzoneAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserInfoComponent,
        UserInfoDetailComponent,
        UserInfoUpdateComponent,
        UserInfoDeleteDialogComponent,
        UserInfoDeletePopupComponent
    ],
    entryComponents: [UserInfoComponent, UserInfoUpdateComponent, UserInfoDeleteDialogComponent, UserInfoDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThelearningzoneUserInfoModule {}
