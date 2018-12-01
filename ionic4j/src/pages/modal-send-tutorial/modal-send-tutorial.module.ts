import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ModalSendTutorialPage } from './modal-send-tutorial';

@NgModule({
  declarations: [
    ModalSendTutorialPage,
  ],
  imports: [
    IonicPageModule.forChild(ModalSendTutorialPage),
  ],
})
export class ModalSendTutorialPageModule {}
