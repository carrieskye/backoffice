/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BackofficeTestModule } from '../../../test.module';
import { DeviceComponent } from 'app/entities/device/device.component';
import { DeviceService } from 'app/entities/device/device.service';
import { Device } from 'app/shared/model/device.model';

describe('Component Tests', () => {
    describe('Device Management Component', () => {
        let comp: DeviceComponent;
        let fixture: ComponentFixture<DeviceComponent>;
        let service: DeviceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BackofficeTestModule],
                declarations: [DeviceComponent],
                providers: []
            })
                .overrideTemplate(DeviceComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DeviceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DeviceService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Device(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.devices[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
