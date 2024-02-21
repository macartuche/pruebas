import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CatalogoDetailComponent } from './catalogo-detail.component';

describe('Catalogo Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CatalogoDetailComponent,
              resolve: { catalogo: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CatalogoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load catalogo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CatalogoDetailComponent);

      // THEN
      expect(instance.catalogo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
