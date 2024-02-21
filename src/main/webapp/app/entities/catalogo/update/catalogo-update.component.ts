import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICatalogo } from '../catalogo.model';
import { CatalogoService } from '../service/catalogo.service';
import { CatalogoFormService, CatalogoFormGroup } from './catalogo-form.service';

@Component({
  standalone: true,
  selector: 'jhi-catalogo-update',
  templateUrl: './catalogo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CatalogoUpdateComponent implements OnInit {
  isSaving = false;
  catalogo: ICatalogo | null = null;

  editForm: CatalogoFormGroup = this.catalogoFormService.createCatalogoFormGroup();

  constructor(
    protected catalogoService: CatalogoService,
    protected catalogoFormService: CatalogoFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogo }) => {
      this.catalogo = catalogo;
      if (catalogo) {
        this.updateForm(catalogo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogo = this.catalogoFormService.getCatalogo(this.editForm);
    if (catalogo.id !== null) {
      this.subscribeToSaveResponse(this.catalogoService.update(catalogo));
    } else {
      this.subscribeToSaveResponse(this.catalogoService.create(catalogo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogo>>): void {
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

  protected updateForm(catalogo: ICatalogo): void {
    this.catalogo = catalogo;
    this.catalogoFormService.resetForm(this.editForm, catalogo);
  }
}
