import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHomePage } from 'app/shared/model/home-page.model';

type EntityResponseType = HttpResponse<IHomePage>;
type EntityArrayResponseType = HttpResponse<IHomePage[]>;

@Injectable({ providedIn: 'root' })
export class HomePageService {
    public resourceUrl = SERVER_API_URL + 'api/home-pages';

    constructor(protected http: HttpClient) {}

    create(homePage: IHomePage): Observable<EntityResponseType> {
        return this.http.post<IHomePage>(this.resourceUrl, homePage, { observe: 'response' });
    }

    update(homePage: IHomePage): Observable<EntityResponseType> {
        return this.http.put<IHomePage>(this.resourceUrl, homePage, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IHomePage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHomePage[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
