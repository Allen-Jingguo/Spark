// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

const getList = async function (
  params: {
    name?: string;
    stauts?: string;
    type?: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/flow/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

export async function stop(
  data: {
    name?: string;
    id?: string;
  },
  options?: { [key: string]: any },
) {
  return request<API.FLow>('/api/flow/stop', {
    method: 'post',
    data: {
      ...data,
    },
    ...(options || {}),
  });
}

export async function start(
  data: {
    name?: string;
    id?: string;
  },
  options?: { [key: string]: any },
) {
  return request<API.FLow>('/api/flow/start', {
    method: 'post',
    data: {
      ...data,
    },
    ...(options || {}),
  });
}

export async function pause(
  data: {
    name?: string;
    id?: string;
  },
  options?: { [key: string]: any },
) {
  return request<API.FLow>('/api/flow/pause', {
    method: 'post',
    data: {
      ...data,
    },
    ...(options || {}),
  });
}

export async function createNew(
  data:any,
  options?: { [key: string]: any },
) {
  return request<any>('/api/flow/create_new', {
    method: 'post',
    data: data,
    ...(options || {}),
  });
}

const getAllInbound = async function () {
  return request<API.KeyValue>('/api/flow/get_inbounds', {
    method: 'GET',
  });
};

const getAllFlowType = async function () {
  return request<API.KeyValue>('/api/flow/get_flow_types', {
    method: 'GET',
  });
};

const getAllParserType = async function () {
  return request<API.KeyValue>('/api/flow/get_parsers', {
    method: 'GET',
  });
};

const getAllKeyMapper = async function () {
  return request<API.KeyValue>('/api/flow/get_key_mappers', {
    method: 'GET',
  });
};

const getAllFormatter = async function () {
  return request<API.KeyValue>('/api/flow/get_formatters', {
    method: 'GET',
  });
};

const getOutboundName = async function () {
  return request<API.KeyValue>('/api/flow/get_outbounds', {
    method: 'GET',
  });
};

export default {
  getList,
  start,
  pause,
  stop,
  createNew,
  getAllInbound,
  getAllFlowType,
  getAllParserType,
  getAllKeyMapper,
  getAllFormatter,
  getOutboundName,
};
