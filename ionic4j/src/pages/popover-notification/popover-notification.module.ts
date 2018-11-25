import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PopoverNotificationPage } from './popover-notification';

@NgModule({
  declarations: [
    PopoverNotificationPage,
  ],
  imports: [
    IonicPageModule.forChild(PopoverNotificationPage),
  ],
})
export class PopoverNotificationPageModule {}
