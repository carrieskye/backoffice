import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IClassification } from 'app/shared/model/classification.model';
import { Moment } from 'moment';
import { IActivity } from 'app/shared/model/activity.model';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<IActivity>;
type EntityArrayResponseType = HttpResponse<Map<Moment, IActivity>>;

@Injectable({
    providedIn: 'root'
})
export class ActivityService {
    public resourceUrl = SERVER_API_URL + 'api/statistics/activity';

    constructor(protected http: HttpClient) {}

    // query(req?: any): Observable<any[]> {
    //     const options = createRequestOption(req);
    //     return <Observable<any[]>> this.http.get(this.resourceUrl, {params: options, observe: 'body'})
    // }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<Map<Moment, IActivity>>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertHashMapFromServer(res)));
    }

    protected convertHashMapFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        console.log('body', res.body);
        const hashMap = <Map<Moment, IActivity>>res.body;
        console.log('A HASHMAP', hashMap);
        console.log('size', hashMap.size);

        if (res.body) {
            res.body.forEach((value, key) => {
                console.log(key, value);
                // classification.timestamp = classification.timestamp != null ? moment(classification.timestamp) : null;
            });
        }
        return res;
    }
}
