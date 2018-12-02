import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { AdminBookingAssignPage } from './admin-booking-assign';

@NgModule({
  declarations: [
    AdminBookingAssignPage,
  ],
  imports: [
    IonicPageModule.forChild(AdminBookingAssignPage),
  ],
})
export class AdminBookingAssignPageModule {}
