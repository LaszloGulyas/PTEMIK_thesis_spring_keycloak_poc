import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BusinessSuperuserComponent } from './business-superuser.component';

describe('BusinessSuperuserComponent', () => {
  let component: BusinessSuperuserComponent;
  let fixture: ComponentFixture<BusinessSuperuserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BusinessSuperuserComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BusinessSuperuserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
