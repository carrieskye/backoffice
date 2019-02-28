/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BackofficeTestModule } from '../../../test.module';
import { HomePageComponent } from 'app/entities/home-page/home-page.component';
import { HomePageService } from 'app/entities/home-page/home-page.service';
import { HomePage } from 'app/shared/model/home-page.model';

describe('Component Tests', () => {
    describe('HomePage Management Component', () => {
        let comp: HomePageComponent;
        let fixture: ComponentFixture<HomePageComponent>;
        let service: HomePageService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BackofficeTestModule],
                declarations: [HomePageComponent],
                providers: []
            })
                .overrideTemplate(HomePageComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HomePageComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HomePageService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new HomePage(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.homePages[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
