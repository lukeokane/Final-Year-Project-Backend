import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ViewController } from 'ionic-angular';


@IonicPage()
@Component({
  selector: 'page-popover-notification',
  templateUrl: 'popover-notification.html',
})
export class PopoverNotificationPage {

  constructor(public viewCtrl: ViewController, public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad PopoverNotificationPage');
  }

  close() {
    this.viewCtrl.dismiss();
  }


}
