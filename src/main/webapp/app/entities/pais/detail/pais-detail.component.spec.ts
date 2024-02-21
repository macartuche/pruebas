import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaisDetailComponent } from './pais-detail.component';

describe('Pais Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaisDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PaisDetailComponent,
              resolve: { pais: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PaisDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load pais on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaisDetailComponent);

      // THEN
      expect(instance.pais).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
