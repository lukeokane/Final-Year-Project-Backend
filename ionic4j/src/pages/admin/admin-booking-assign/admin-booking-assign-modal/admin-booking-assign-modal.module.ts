import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { AdminBookingAssignModalPage } from './admin-booking-assign-modal';

@NgModule({
  declarations: [
    AdminBookingAssignModalPage,
  ],
  imports: [
    IonicPageModule.forChild(AdminBookingAssignModalPage),
  ],
})
export class AdminBookingAssignModalPageModule {}
