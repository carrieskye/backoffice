import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { Moment } from 'moment';
import { IActivity } from 'app/shared/model/activity.model';
import { IAgeDistribution } from 'app/shared/model/age-distribution.model';

@Injectable({
    providedIn: 'root'
})
export class AgeDistributionService {
    public resourceUrl = SERVER_API_URL + 'api/statistics/activity/age';

    constructor(protected http: HttpClient) {}

    query(store: number, interval?: number): Observable<any> {
        let url = this.resourceUrl;
        url += interval ? '?interval=' + interval + '&store=' + store : '?store=' + store;
        return this.http.get<Map<Moment, IAgeDistribution>>(url, { observe: 'body' });
    }
}
