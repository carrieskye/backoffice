import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Moment } from 'moment';
import { IActivity } from 'app/shared/model/activity.model';

@Injectable({
    providedIn: 'root'
})
export class ActivityService {
    public resourceUrl = SERVER_API_URL + 'api/statistics/activity';

    constructor(protected http: HttpClient) {}

    query(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<Map<Moment, IActivity>>(this.resourceUrl, { params: options, observe: 'body' });
    }
}
