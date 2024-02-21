import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { PaisService } from '../service/pais.service';
import { IPais } from '../pais.model';
import { PaisFormService } from './pais-form.service';

import { PaisUpdateComponent } from './pais-update.component';

describe('Pais Management Update Component', () => {
  let comp: PaisUpdateComponent;
  let fixture: ComponentFixture<PaisUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paisFormService: PaisFormService;
  let paisService: PaisService;
  let itemService: ItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PaisUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PaisUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaisUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paisFormService = TestBed.inject(PaisFormService);
    paisService = TestBed.inject(PaisService);
    itemService = TestBed.inject(ItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call region query and add missing value', () => {
      const pais: IPais = { id: 456 };
      const region: IItem = { id: 26394 };
      pais.region = region;

      const regionCollection: IItem[] = [{ id: 24353 }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
      const expectedCollection: IItem[] = [region, ...regionCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(regionCollection, region);
      expect(comp.regionsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pais: IPais = { id: 456 };
      const region: IItem = { id: 19244 };
      pais.region = region;

      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      expect(comp.regionsCollection).toContain(region);
      expect(comp.pais).toEqual(pais);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPais>>();
      const pais = { id: 123 };
      jest.spyOn(paisFormService, 'getPais').mockReturnValue(pais);
      jest.spyOn(paisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pais }));
      saveSubject.complete();

      // THEN
      expect(paisFormService.getPais).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paisService.update).toHaveBeenCalledWith(expect.objectContaining(pais));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPais>>();
      const pais = { id: 123 };
      jest.spyOn(paisFormService, 'getPais').mockReturnValue({ id: null });
      jest.spyOn(paisService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pais: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pais }));
      saveSubject.complete();

      // THEN
      expect(paisFormService.getPais).toHaveBeenCalled();
      expect(paisService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPais>>();
      const pais = { id: 123 };
      jest.spyOn(paisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paisService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareItem', () => {
      it('Should forward to itemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(itemService, 'compareItem');
        comp.compareItem(entity, entity2);
        expect(itemService.compareItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
