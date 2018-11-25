import { Component } from '@angular/core';

/**
 * Generated class for the TopMenuComponent component.
 *
 * See https://angular.io/api/core/Component for more info on Angular
 * Components.
 */
@Component({
  selector: 'top-menu',
  templateUrl: 'top-menu.html'
})
export class TopMenuComponent {

  text: string;

  constructor() {
    console.log('Hello TopMenuComponent Component');
    this.text = 'Hello World';
  }

}
