import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { AdminStatisticsPage } from './admin-statistics';

@NgModule({
  declarations: [
    AdminStatisticsPage,
  ],
  imports: [
    IonicPageModule.forChild(AdminStatisticsPage),
  ],
})
export class AdminStatisticsPageModule {}
