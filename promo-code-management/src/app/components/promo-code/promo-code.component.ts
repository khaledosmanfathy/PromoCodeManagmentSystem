import { Component, OnInit } from '@angular/core';
import { PromoCodeService } from '../../services/promo-code.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-promo-code',
  standalone: true,
  imports: [CommonModule, FormsModule,HttpClientModule],
  templateUrl: './promo-code.html',
  styleUrls: ['./promo-code.css']
})
export class PromoCodeComponent implements OnInit {
  promoCodes: any[] = [];
  newPromoCode: any = {};
  applyCode: string = '';
  applyResult: any;

  constructor(private promoCodeService: PromoCodeService) {}

  ngOnInit() {
    this.loadPromoCodes();
  }

  loadPromoCodes() {
    this.promoCodeService.getPromoCodes().subscribe(data => {
      this.promoCodes = data;
    });
  }

  createPromoCode() {
    this.promoCodeService.createPromoCode(this.newPromoCode).subscribe(() => {
      this.loadPromoCodes();
      this.newPromoCode = {};
    });
  }

  applyPromoCode() {
    this.promoCodeService.applyPromoCode(this.applyCode).subscribe(result => {
      this.applyResult = result;
    });
  }
}