import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { UserComponent } from './user/user.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { ObuDetailComponent } from './obu-detail/obu-detail.component';
import { ConfigurationDetailComponent } from './configuration-detail/configuration-detail.component';
import { HomemapComponent } from './homemap/homemap.component';
import { NavMenuComponent } from './nav-menu/nav-menu.component';
import { AuthGuard } from './authguard/auth.guard';
import { UserProfile } from './Model/UserProfile';
import { OBUComponent } from './obu/obu.component'
import { LogoutComponent } from './logout/logout.component';
import { ConfigurationComponent } from './configuration/configuration.component';
import { TestPlansComponent } from './test-plans/test-plans.component'
import { HardwareComponent } from './hardware/hardware.component'
import { HardwareDetailComponent } from './hardware-detail/hardware-detail.component';
import { HardwareCreateComponent } from './hardware-create/hardware-create.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent },
          
  { path: 'home', redirectTo: '/home/map', pathMatch: 'full' },
  { path: 'home', component: NavMenuComponent,
      children: [
        { path: 'map', component: HomemapComponent, canActivate: [AuthGuard] },
        { path: 'users', component: UserComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SUPER_USER }},
        { path: 'hardwares', component: HardwareComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SUPER_USER }},
        { path: 'hardware/:id/edit', component: HardwareDetailComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SUPER_USER }},
        { path: 'hardware/create', component: HardwareCreateComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SUPER_USER }},
        { path: 'obus', component: OBUComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SUPER_USER }},
        { path: 'user/:id/edit', component: UserDetailComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SUPER_USER }},
        { path: 'obu/:id/edit', component: ObuDetailComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SUPER_USER }},
        { path: 'configs', component: ConfigurationComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SUPER_USER }},
        { path: 'config/:id/edit', component: ConfigurationDetailComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SUPER_USER }},
        { path: 'testPlans', component: TestPlansComponent, canActivate: [AuthGuard], data: { min_user_profile: UserProfile.SUPER_USER }},
        
      ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
