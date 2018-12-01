import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { SelectTimeslotPage } from './select-timeslot';

@NgModule({
  declarations: [
    SelectTimeslotPage,
  ],
  imports: [
    IonicPageModule.forChild(SelectTimeslotPage),
  ],
})
export class SelectTimeslotPageModule {}
