import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


import { LoginComponent } from './login/login.component';
import { UserComponent } from './user/user.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { HomemapComponent } from './homemap/homemap.component';
import { NavMenuComponent } from './nav-menu/nav-menu.component';
import { AuthGuard } from './auth/auth.guard';
import { UserProfile } from './Model/UserProfile';
import { OBUComponent } from './obu/obu.component'
import { LogoutComponent } from './logout/logout.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent },
          
  { path: 'home', redirectTo: '/home/map', pathMatch: 'full' },
  { path: 'home', component: NavMenuComponent,
      children: [
        { path: 'map', component: HomemapComponent, canActivate: [AuthGuard] },
        { path: 'users', component: UserComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SuperUser }},
        { path: 'obus', component: OBUComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SuperUser }},
        { path: 'user/:id/edit', component: UserDetailComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SuperUser }}
  
      ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
