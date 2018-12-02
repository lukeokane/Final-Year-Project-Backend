import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { AdminBookingManagementPage } from './admin-booking-management';

@NgModule({
  declarations: [
    AdminBookingManagementPage,
  ],
  imports: [
    IonicPageModule.forChild(AdminBookingManagementPage),
  ],
})
export class AdminBookingManagementPageModule {}
