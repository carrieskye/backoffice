import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { IAgeDistribution } from 'app/shared/model/age-distribution.model';

@Injectable({
    providedIn: 'root'
})
export class AgeDistributionService {
    public resourceUrl = SERVER_API_URL + 'api/statistics/activity/age';

    constructor(protected http: HttpClient) {}

    query(interval?: number): Observable<any> {
        let url = this.resourceUrl;
        url += interval ? '&interval=' + interval : '';

        return this.http.get<IAgeDistribution[]>(url, { observe: 'body' });
    }
}
