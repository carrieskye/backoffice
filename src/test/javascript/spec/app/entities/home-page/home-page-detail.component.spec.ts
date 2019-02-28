/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BackofficeTestModule } from '../../../test.module';
import { HomePageDetailComponent } from 'app/entities/home-page/home-page-detail.component';
import { HomePage } from 'app/shared/model/home-page.model';

describe('Component Tests', () => {
    describe('HomePage Management Detail Component', () => {
        let comp: HomePageDetailComponent;
        let fixture: ComponentFixture<HomePageDetailComponent>;
        const route = ({ data: of({ homePage: new HomePage(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BackofficeTestModule],
                declarations: [HomePageDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HomePageDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HomePageDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.homePage).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
