import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { Moment } from 'moment';
import { IAgeDistribution } from 'app/shared/model/age-distribution.model';

@Injectable({
    providedIn: 'root'
})
export class AgeDistributionService {
    public resourceUrl = SERVER_API_URL + 'api/statistics/activity/age';

    constructor(protected http: HttpClient) {}

    query(store: number, interval?: number, ageInterval?: number, timeStart?: string, timeEnd?: string): Observable<any> {
        let url = this.resourceUrl;
        url += '?store=' + store;
        url += interval ? '&interval=' + interval : '';
        url += ageInterval ? '&ageInterval=' + ageInterval : '';
        url += timeStart ? '&timeStart=' + timeStart : '';
        url += timeEnd ? '&timeEnd=' + timeEnd : '';
        return this.http.get<Map<Moment, IAgeDistribution>>(url, { observe: 'body' });
    }
}
