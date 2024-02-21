import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICatalogo } from 'app/entities/catalogo/catalogo.model';
import { CatalogoService } from 'app/entities/catalogo/service/catalogo.service';
import { IItem } from '../item.model';
import { ItemService } from '../service/item.service';
import { ItemFormService, ItemFormGroup } from './item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-item-update',
  templateUrl: './item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ItemUpdateComponent implements OnInit {
  isSaving = false;
  item: IItem | null = null;

  catalogosSharedCollection: ICatalogo[] = [];

  editForm: ItemFormGroup = this.itemFormService.createItemFormGroup();

  constructor(
    protected itemService: ItemService,
    protected itemFormService: ItemFormService,
    protected catalogoService: CatalogoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCatalogo = (o1: ICatalogo | null, o2: ICatalogo | null): boolean => this.catalogoService.compareCatalogo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.item = item;
      if (item) {
        this.updateForm(item);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const item = this.itemFormService.getItem(this.editForm);
    if (item.id !== null) {
      this.subscribeToSaveResponse(this.itemService.update(item));
    } else {
      this.subscribeToSaveResponse(this.itemService.create(item));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(item: IItem): void {
    this.item = item;
    this.itemFormService.resetForm(this.editForm, item);

    this.catalogosSharedCollection = this.catalogoService.addCatalogoToCollectionIfMissing<ICatalogo>(
      this.catalogosSharedCollection,
      item.catalogo,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogoService
      .query()
      .pipe(map((res: HttpResponse<ICatalogo[]>) => res.body ?? []))
      .pipe(
        map((catalogos: ICatalogo[]) => this.catalogoService.addCatalogoToCollectionIfMissing<ICatalogo>(catalogos, this.item?.catalogo)),
      )
      .subscribe((catalogos: ICatalogo[]) => (this.catalogosSharedCollection = catalogos));
  }
}
