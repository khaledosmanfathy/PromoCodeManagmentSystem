import { TestBed } from '@angular/core/testing';

import { PromoCode } from './promo-code';

describe('PromoCode', () => {
  let service: PromoCode;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PromoCode);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
