import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { IAgeDistribution } from 'app/shared/model/age-distribution.model';

@Injectable({
    providedIn: 'root'
})
export class AgeDistributionService {
    public resourceUrl = SERVER_API_URL + 'api/statistics/agedistribution';

    constructor(protected http: HttpClient) {}

    query(start: Date, end: Date, interval?: number): Observable<any> {
        let url = this.resourceUrl;
        url += '?start=' + ('0' + start.getDate()).slice(-2) + ('0' + (start.getMonth() + 1)).slice(-2) + start.getFullYear();
        url += '&end=' + ('0' + end.getDate()).slice(-2) + ('0' + (end.getMonth() + 1)).slice(-2) + end.getFullYear();
        url += interval ? '&interval=' + interval : '';

        return this.http.get<IAgeDistribution[]>(url, { observe: 'body' });
    }
}
