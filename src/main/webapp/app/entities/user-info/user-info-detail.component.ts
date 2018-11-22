import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserInfo } from 'app/shared/model/user-info.model';

@Component({
    selector: 'jhi-user-info-detail',
    templateUrl: './user-info-detail.component.html'
})
export class UserInfoDetailComponent implements OnInit {
    userInfo: IUserInfo;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userInfo }) => {
            this.userInfo = userInfo;
        });
    }

    previousState() {
        window.history.back();
    }
}
