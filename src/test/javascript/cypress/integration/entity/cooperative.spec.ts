import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Cooperative e2e test', () => {
  const cooperativePageUrl = '/cooperative';
  const cooperativePageUrlPattern = new RegExp('/cooperative(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const cooperativeSample = { id: 'dd60cd60-2591-4157-8e07-fc34f33372f3', name: 'microchip Fish Gorgeous' };

  let cooperative: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cooperatives+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cooperatives').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cooperatives/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cooperative) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cooperatives/${cooperative.id}`,
      }).then(() => {
        cooperative = undefined;
      });
    }
  });

  it('Cooperatives menu should load Cooperatives page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cooperative');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cooperative').should('exist');
    cy.url().should('match', cooperativePageUrlPattern);
  });

  describe('Cooperative page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cooperativePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cooperative page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cooperative/new$'));
        cy.getEntityCreateUpdateHeading('Cooperative');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cooperativePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cooperatives',
          body: cooperativeSample,
        }).then(({ body }) => {
          cooperative = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cooperatives+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cooperative],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cooperativePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Cooperative page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cooperative');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cooperativePageUrlPattern);
      });

      it('edit button click should load edit Cooperative page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cooperative');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cooperativePageUrlPattern);
      });

      it('last delete button click should delete instance of Cooperative', () => {
        cy.intercept('GET', '/api/cooperatives/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cooperative').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cooperativePageUrlPattern);

        cooperative = undefined;
      });
    });
  });

  describe('new Cooperative page', () => {
    beforeEach(() => {
      cy.visit(`${cooperativePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cooperative');
    });

    it('should create an instance of Cooperative', () => {
      cy.get(`[data-cy="id"]`).type('31be8bc9-0b68-4f7a-b1ec-35e4be702cd1').should('have.value', '31be8bc9-0b68-4f7a-b1ec-35e4be702cd1');

      cy.get(`[data-cy="name"]`).type('Games deposit').should('have.value', 'Games deposit');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cooperative = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cooperativePageUrlPattern);
    });
  });
});
