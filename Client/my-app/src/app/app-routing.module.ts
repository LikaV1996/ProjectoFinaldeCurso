import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


import { LoginComponent } from './login/login.component';
import { UserComponent } from './user/user.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { HomemapComponent } from './homemap/homemap.component';
import { NavMenuComponent } from './nav-menu/nav-menu.component';
import { AuthGuard } from './auth/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  
  { path: 'home', redirectTo: '/home/map', pathMatch: 'full' },
  { path: 'home', component: NavMenuComponent,
      children: [
        { path: 'map', component: HomemapComponent, canActivate: [AuthGuard] },
        { path: 'users', component: UserComponent, canActivate: [AuthGuard] },
        { path: 'user/:id', component: UserDetailComponent, canActivate: [AuthGuard] }
      ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
