import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ItlcHomePage } from './itlc-home';

@NgModule({
  declarations: [
    ItlcHomePage,
  ],
  imports: [
    IonicPageModule.forChild(ItlcHomePage),
  ],
})
export class ItlcHomePageModule {}
