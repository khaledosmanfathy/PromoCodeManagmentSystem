import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PromoCodeComponent } from './components/promo-code/promo-code.component';
import { FormsModule } from '@angular/forms';

// import { ReportComponent } from './components/report/report.component';
export const routes: Routes = [
  { path: 'promo-codes', component: PromoCodeComponent },
//   { path: 'report', component: ReportComponent },
  { path: '', redirectTo: '/promo-codes', pathMatch: 'full' }
];
@NgModule({
  imports: [RouterModule.forRoot(routes),FormsModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }