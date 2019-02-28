/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ClassificationService } from 'app/entities/classification/classification.service';
import { IClassification, Classification, Gender, Emotion } from 'app/shared/model/classification.model';

describe('Service Tests', () => {
    describe('Classification Service', () => {
        let injector: TestBed;
        let service: ClassificationService;
        let httpMock: HttpTestingController;
        let elemDefault: IClassification;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ClassificationService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Classification(0, 'AAAAAAA', currentDate, 0, Gender.MALE, Emotion.ANGRY);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        timestamp: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Classification', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        timestamp: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        timestamp: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Classification(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Classification', async () => {
                const returnedFromService = Object.assign(
                    {
                        personId: 'BBBBBB',
                        timestamp: currentDate.format(DATE_TIME_FORMAT),
                        age: 1,
                        gender: 'BBBBBB',
                        emotion: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        timestamp: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Classification', async () => {
                const returnedFromService = Object.assign(
                    {
                        personId: 'BBBBBB',
                        timestamp: currentDate.format(DATE_TIME_FORMAT),
                        age: 1,
                        gender: 'BBBBBB',
                        emotion: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        timestamp: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Classification', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
