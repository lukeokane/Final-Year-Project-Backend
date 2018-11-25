import { Component } from '@angular/core';
import { PopoverController } from 'ionic-angular';
import { PopoverNotificationPage } from '../../pages/popover-notification/popover-notification';

@Component({
  selector: 'top-menu',
  templateUrl: 'top-menu.html'
})
export class TopMenuComponent {

  text: string;

  constructor(public popoverCtrl: PopoverController) {
  }

  showNotifications(myEvent) {
    console.log(myEvent);
    const popover = this.popoverCtrl.create(PopoverNotificationPage,{},{cssClass:'custom-popover'});
    // myEvent={
    //   target : {
    //     getBoundingClientRect : () => {
    //       return {
    //         top: '100'
    //       };
    //     }
    //   }
    // };
    
    // const popover = this.popoverCtrl.create({PopoverNotificationPage,  { cssClass: 'custom-popover' }});
    popover.present({ ev: myEvent });
  }
}
