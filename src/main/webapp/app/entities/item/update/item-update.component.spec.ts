import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICatalogo } from 'app/entities/catalogo/catalogo.model';
import { CatalogoService } from 'app/entities/catalogo/service/catalogo.service';
import { ItemService } from '../service/item.service';
import { IItem } from '../item.model';
import { ItemFormService } from './item-form.service';

import { ItemUpdateComponent } from './item-update.component';

describe('Item Management Update Component', () => {
  let comp: ItemUpdateComponent;
  let fixture: ComponentFixture<ItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemFormService: ItemFormService;
  let itemService: ItemService;
  let catalogoService: CatalogoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ItemUpdateComponent],
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
      .overrideTemplate(ItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemFormService = TestBed.inject(ItemFormService);
    itemService = TestBed.inject(ItemService);
    catalogoService = TestBed.inject(CatalogoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Catalogo query and add missing value', () => {
      const item: IItem = { id: 456 };
      const catalogo: ICatalogo = { id: 31075 };
      item.catalogo = catalogo;

      const catalogoCollection: ICatalogo[] = [{ id: 2617 }];
      jest.spyOn(catalogoService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogoCollection })));
      const additionalCatalogos = [catalogo];
      const expectedCollection: ICatalogo[] = [...additionalCatalogos, ...catalogoCollection];
      jest.spyOn(catalogoService, 'addCatalogoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(catalogoService.query).toHaveBeenCalled();
      expect(catalogoService.addCatalogoToCollectionIfMissing).toHaveBeenCalledWith(
        catalogoCollection,
        ...additionalCatalogos.map(expect.objectContaining),
      );
      expect(comp.catalogosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const item: IItem = { id: 456 };
      const catalogo: ICatalogo = { id: 241 };
      item.catalogo = catalogo;

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(comp.catalogosSharedCollection).toContain(catalogo);
      expect(comp.item).toEqual(item);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue(item);
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemService.update).toHaveBeenCalledWith(expect.objectContaining(item));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue({ id: null });
      jest.spyOn(itemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(itemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCatalogo', () => {
      it('Should forward to catalogoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(catalogoService, 'compareCatalogo');
        comp.compareCatalogo(entity, entity2);
        expect(catalogoService.compareCatalogo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
