import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Moment } from 'moment';
import { SERVER_API_URL } from 'app/app.constants';
import { IActivity } from 'app/shared/model/activity.model';

@Injectable({
    providedIn: 'root'
})
export class ActivityService {
    public resourceUrl = SERVER_API_URL + 'api/statistics/activity';

    constructor(protected http: HttpClient) {}

    query(interval?: number): Observable<any> {
        let url = this.resourceUrl;
        url += interval ? '?interval=' + interval : '';
        return this.http.get<Map<Moment, IActivity>>(url, { observe: 'body' });
    }
}
