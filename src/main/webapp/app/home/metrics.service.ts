import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class MetricsService {
    constructor(private http: HttpClient) {}

    all(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/admin/metrics');
    }
}
