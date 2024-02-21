import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IPais } from '../pais.model';
import { PaisService } from '../service/pais.service';
import { PaisFormService, PaisFormGroup } from './pais-form.service';

@Component({
  standalone: true,
  selector: 'jhi-pais-update',
  templateUrl: './pais-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaisUpdateComponent implements OnInit {
  isSaving = false;
  pais: IPais | null = null;

  regionsCollection: IItem[] = [];

  editForm: PaisFormGroup = this.paisFormService.createPaisFormGroup();

  constructor(
    protected paisService: PaisService,
    protected paisFormService: PaisFormService,
    protected itemService: ItemService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pais }) => {
      this.pais = pais;
      if (pais) {
        this.updateForm(pais);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pais = this.paisFormService.getPais(this.editForm);
    if (pais.id !== null) {
      this.subscribeToSaveResponse(this.paisService.update(pais));
    } else {
      this.subscribeToSaveResponse(this.paisService.create(pais));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPais>>): void {
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

  protected updateForm(pais: IPais): void {
    this.pais = pais;
    this.paisFormService.resetForm(this.editForm, pais);

    this.regionsCollection = this.itemService.addItemToCollectionIfMissing<IItem>(this.regionsCollection, pais.region);
  }

  protected loadRelationshipsOptions(): void {
    this.itemService
      .query({ filter: 'pais-is-null' })
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, this.pais?.region)))
      .subscribe((items: IItem[]) => (this.regionsCollection = items));
  }
}
