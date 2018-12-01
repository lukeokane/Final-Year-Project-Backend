import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { RequestTutorialPage } from './request-tutorial';

@NgModule({
  declarations: [
    RequestTutorialPage,
  ],
  imports: [
    IonicPageModule.forChild(RequestTutorialPage),
  ],
})
export class RequestTutorialPageModule {}
